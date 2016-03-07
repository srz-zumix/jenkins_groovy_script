def jenkins = hudson.model.Hudson.instance
def slaves = jenkins.slaves
slaves.each {
  def com = it.toComputer()
  def envvars = com.getEnvironment()
  if( envvars != null ) {
    if( envvars.get("") != null ) {
      println(it.getNodeName())
    }
  }
}
