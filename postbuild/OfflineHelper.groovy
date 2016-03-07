import hudson.slaves.OfflineCause

static checkOfflineNodes(manager)
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

static toOffline(manager, message)
{
  def com = manager.build.getBuiltOn().toComputer()
  if( com.isOnline() ) {
    com.setTemporarilyOffline(true, new OfflineCause.ByCLI(message))
  }
}

static toOfflineCausedByResult(manager)
{
  if( manager.build.getResult().isWorseThan(hudson.model.Result.SUCCESS) ) {
    def msg = "automated offline: ${manager.build.getProject().getDisplayName()}#${manager.build.number}"
    toOffline(manager, msg)
  }
}

static toOfflineCausedByFindText(manager, regexp)
{
  if( manager.logContains(regexp) ) {
    def msg = "automated offline: ${manager.build.getProject().getDisplayName()}#${manager.build.number}"
    toOffline(manager, msg)
  }
}

