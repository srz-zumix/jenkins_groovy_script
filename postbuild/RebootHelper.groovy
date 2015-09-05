import hudson.slaves.OfflineCause
import hudson.util.RemotingDiagnostics

static reboot(manager)
{
  def jenkins = hudson.model.Hudson.instance

  def offline_msg = "automated reboot at end of test"
  def target_name = manager.build.buildVariables.get("REBOOT_SLAVE")
  if ( target_name == null ) {
      manager.listener.logger.println("ERROR!! REBOOT_SLAVE is null.")
  }

  // 同一 PC 上のスレーブをオフライン
  def slaves = jenkins.slaves
  slaves.each {
      def com = it.toComputer()
      def name = com.getEnvironment().get("COMPUTERNAME","")
      name = name.toLowerCase()
      if( name.compareToIgnoreCase(target_name) == 0 ) {
          if( com.isOnline() ) {
              // スレーブをオフラインにしてアイドル待ち
              com.setTemporarilyOffline(true, new OfflineCause.ByCLI(offline_msg))
              // 実際にアイドル待ちするのは、全部オフラインにしてから
          }
      }
  }

  slaves.each {
      def com = it.toComputer()
      def name = com.getEnvironment().get("COMPUTERNAME","")
      name = name.toLowerCase()
      def node_name = it.getNodeName()
      if( name.compareToIgnoreCase(target_name) == 0 ) {
          if( !com.isIdle() ) {
              manager.listener.logger.println("Wait ${node_name} Slave Task...")
              while( !com.isIdle() ) {
                  Thread.sleep(3000)
                  if( com.isOnline() ) {
                    // スレーブをオフラインに戻す
                    com.setTemporarilyOffline(true, new OfflineCause.ByCLI(offline_msg))
                  }
              }
          }
      }
  }

  manager.listener.logger.println("shutdown")
  def r = "cmd /c \"shutdown /f /r /t 10 -m \\\\\\\\${target_name} /c \"Restarting after Jenkins test completed\"\"".execute().waitFor()
  if( r != 0 ) {
    manager.listener.logger.println(r)
    manager.buildFailure()
    return
  }

  Thread.sleep(5*60*1000)

  for( i in 0..3 ) {

    manager.listener.logger.println("wait restart...")

    // 再起動待ち
    Thread.sleep(5*60*1000)

    // 再接続
    def online = true
    slaves.each {
        def com = it.toComputer()
        def name = com.getEnvironment().get("COMPUTERNAME","")
        name = name.toLowerCase()
        if( name.compareToIgnoreCase(target_name) == 0 ) {
            com.setTemporarilyOffline(false, com.getOfflineCause())
            if( com.isOffline() ) {
              def node_name = it.getNodeName()
              manager.listener.logger.println("${node_name} is still offline...")
              online = flase
            }
        }
    }

    if( online ) {
      manager.listener.logger.println("OK.")
      break
    }

  }
}

static reboot_self(manager)
{
  def computer = manager.build.getBuiltOn().toComputer()
  def channel = computer.getChannel()
  manager.listener.logger.println "reboot windows"
  RemotingDiagnostics.executeGroovy( """
   
      if (Functions.isWindows()) {
        'shutdown /r /t 10 /c "Restarting after Jenkins test completed"'.execute()
      } else {
        "sudo reboot".execute()
      }
   
  """, channel )
}

