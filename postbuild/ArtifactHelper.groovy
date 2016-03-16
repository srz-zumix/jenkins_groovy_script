static createArtifactImageSummary(manager)
{
  def a = manager.build.getArtifacts()
  a.each {
    def path = it.relativePath
    if( path ==~ /.*\.(png|jpeg|jpg|bmp)/ ) {
      manager.listener.logger.println(path)
      manager.createSummary("clipboard.gif").appendText("<img src=\"artifact/$path\"><br />$path", false, false, false, "red")
    }
  }
}
