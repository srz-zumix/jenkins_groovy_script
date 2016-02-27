import hudson.model.Cause.UpstreamCause
import hudson.model.AbstractBuild
import hudson.scm.SubversionChangeLogSet

static addSVNRevisionShortText(manager)
{
  def cause = manager.build.getCause(UpstreamCause.class)
  while( cause ) {
    def run = cause.getUpstreamRun()
    if( run instanceof AbstractBuild) {
      def build = run
      def c = build.getChangeSet()
      if( c instanceof SubversionChangeLogSet ) {
        def m = c.revisionMap
        if( m.size() == 1 ) {
          m.each { k,v ->
            manager.addShortText("r${v}")
          }
          return
        }
      }
      cause = build.getCause(UpstreamCause.class)
    } else {
      return
    }
  }
}
