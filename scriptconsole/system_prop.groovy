def jenkins = hudson.model.Hudson.instance
def slaves = jenkins.slaves
slaves.each {
  def com = it.toComputer()
  def properties = com.getSystemProperties()
  if( properties != null )
  {
    println(it.getNodeName())
    println(properties[prop])
  }
}
