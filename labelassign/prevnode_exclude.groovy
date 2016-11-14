def label = currentJob.assignedLabel
def last = currentJob.getLastBuild()
if( last == null ) return
def node = last.getBuiltOnStr()
if( node == null || node == "" ) node = "master"
if( node ) {
  expr = "${label}&&!" + node
  newlabel = hudson.model.Label.parseExpression(expr)
  if(newlabel.isAssignable()) {
    return expr
  }
}
