import hudson.tasks.test.AbstractTestResultAction

static addTestResultDescription(manager)
{
	//def act = manager.build.getTestResultAction()
	def act = manager.build.getAction(AbstractTestResultAction.class)
	if( act == null ) {
		manager.listener.logger.println "testResultAction not found"
		return
	}
	def tr = act.getResult()
	tr.description = "built on ${manager.build.getBuiltOn().getNodeName()}"
	def st = tr.getSuites()
	st.each { suite ->
		def cr = suite.getCases()
		cr.each { res ->
			res.description = "${manager.build.getTime().format("MM/dd(E) HH:mm::ss")}"
		}
	}
}

