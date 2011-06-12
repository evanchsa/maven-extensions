package org.objectledge.m2e.scm;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class ScmBuildParticipant extends MojoExecutionBuildParticipant {

	public ScmBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {

		// execute mojo
		Set<IProject> result = super.build(kind, monitor);

		IMaven maven = MavenPlugin.getMaven();
		BuildContext buildContext = getBuildContext();
		MojoExecution mojoExecution = getMojoExecution();
		
		String targetDirectoryParameter = "workingDirectory";
		if ("bootstrap".equals(mojoExecution.getGoal())) {
			targetDirectoryParameter = "checkoutDirectory";
		}
		if ("checkout".equals(mojoExecution.getGoal())) {
			targetDirectoryParameter = "checkoutDirectory";
		} 
		File targetDirectory = maven.getMojoParameterValue(getSession(),
				getMojoExecution(), targetDirectoryParameter, File.class);
		if (targetDirectory != null) {
			buildContext.refresh(targetDirectory);
		}

		return result;
	}
}
