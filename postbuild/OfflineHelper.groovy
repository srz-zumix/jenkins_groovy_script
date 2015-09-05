import hudson.slaves.OfflineCause

static print(manager)
{
  def jenkins = hudson.model.Hudson.instance
  def slaves = jenkins.slaves
  slaves.each {
    def com = it.toComputer()
    def cause = com.getOfflineCause()
    if( cause != null )
    {
        if( "${cause}".find("ChannelTermination") == null )
        {
            manager.listener.logger.println(it.getNodeName())
            manager.listener.logger.println("- ${cause}")
            manager.buildUnstable()
        }
     }
  }
}

static to_offline_caused_by_result(manager)
{
  if( manager.build.getResult().isWorseThan(hudson.model.Result.SUCCESS) ) {
    def msg = "automated offline: ${manager.build.getProject().getDisplayName()}#${manager.build.number}"
    def com = manager.build.getBuiltOn().toComputer()
    if( com.isOnline() ) {
      com.setTemporarilyOffline(true, new OfflineCause.ByCLI(msg))
    }
  }
}

static to_offline(manager, message)
{
  def com = manager.build.getBuiltOn().toComputer()
  if( com.isOnline() ) {
    com.setTemporarilyOffline(true, new OfflineCause.ByCLI(message))
  }
}


