def jenkins = hudson.model.Hudson.instance
def p = jenkins.getPlugin("ircbot")
if( p == null ) {
  println("require IRC Plugin")
  return
}
def c = p.imPlugin.provider.currentConnection()
if( c == null ) {
  println("IRC connection not found. please set to IRC configuration")
  return
}
c.send(irc_channel, irc_message)

