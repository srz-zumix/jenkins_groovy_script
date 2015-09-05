
static keepLog(manager)
{
  if( manager.build.canToggleLogKeep() ) {
    manager.build.keepLog()
  } else {
    def upstreamBuild = manager.build.getUpstreamRelationshipBuild()
    if( upstreamBuild.getNaem() == manager.build.getName() ) {
      if( upstreamBuild.canToggleLogKeep() ) {
        upstreamBuild.keepLog()
      }
    }
  }
}

