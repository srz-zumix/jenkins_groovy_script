
static send(manager, channel, message)
{
  def jenkins = hudson.model.Hudson.instance
  def p = jenkins.getPlugin("ircbot")
  if( p == null ) {
    manager.listener.logger.println("require IRC Plugin")
    return
  }
  def c = p.imPlugin.provider.currentConnection()
  if( p == null ) {
    manager.listener.logger.println("IRC connection not found. please set to IRC configuration")
    return
  }
  c.send(channel, message)
}
