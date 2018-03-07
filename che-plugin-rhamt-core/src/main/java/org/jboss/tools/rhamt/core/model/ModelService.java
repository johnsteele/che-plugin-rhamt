/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.rhamt.core.model;

import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupResult;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.Link;
import org.jboss.windup.tooling.data.Quickfix;
import org.jboss.windup.tooling.data.ReportLink;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ModelService {
	
	private static final Logger LOG = Logger.getLogger(ModelService.class.getName());
	
	private static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss";
	
	private WindupModel model;
	
	@Inject
	public ModelService() {
		load();
	}
	
	@PreDestroy
	private void dispose() {
		save();
	}
	
	public void save() {
		try {
			model.eResource().save(null);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Error saving RHAMT model resource.", e);
		}
	}
	
	private void load() {
		File location = new File(getModelStateLocation());
		Resource resource = createResource();
		if (location.exists()) {
			try {
				resource.load(null);
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Error loading existing RHAMT model.", e);
				return;
			}
			model = (WindupModel)resource.getContents().get(0);
		}
		else {
			model = WindupFactory.eINSTANCE.createWindupModel();
			resource.getContents().add(model);
		}
	}

	private Resource createResource() {
		ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
            (Resource.Factory.Registry.DEFAULT_EXTENSION, 
             new XMIResourceFactoryImpl());
        Resource resource = resourceSet.createResource(URI.createFileURI(getModelStateLocation()));
        resource.setTrackingModification(true);
        return resource;
	}
	
	private String getModelStateLocation() {
		return System.getProperty("user.home") + "/.rhamt/che/model/rhamt.xmi";
	}
	
	private String getReportsStateLocation() {
		return System.getProperty("user.home") + "/.rhamt/che/reports";
	}
	
	public String getGeneratedReportBaseLocation(ConfigurationElement configuration) {
		String reports = getReportsStateLocation();
		String name = configuration.getName().replaceAll("\\s+", "");
		return new StringJoiner("/")
			.add(reports)
			.add(name)
			.toString();
	}
	
	public void createInput(ConfigurationElement configuration, String path) {
		Input input = WindupFactory.eINSTANCE.createInput();
		input.setUri(path);
		configuration.getInputs().add(input);
	}
	
	public ConfigurationElement createConfiguration() {
		Set<String> existingNames = 
			model
				.getConfigurationElements()
				.stream()
				.map(ConfigurationElement::getName)
				.collect(toSet());
		
		String newName = "runConfiguration";
		
		if (existingNames.contains(newName)) {
			for (int count = 1; count < 1000; count++) {
				if (!existingNames.contains(newName + "-" + count)) {
					newName = newName + "-" + count;
					break;
				}
			}
		}
		return createConfiguration(newName);
	}
	
	public ConfigurationElement createConfiguration(String name) {
		ConfigurationElement configuration = WindupFactory.eINSTANCE.createConfigurationElement();
		configuration.setName(name);
		configuration.setWindupHome(System.getenv("RHAMT_HOME"));
		configuration.setGeneratedReportsLocation(getGeneratedReportBaseLocation(configuration));
		configuration.setSourceMode(true);
		configuration.setGenerateReport(true);
		//configuration.setMigrationPath(model.getMigrationPaths().get(1));
		model.getConfigurationElements().add(configuration);
		save();
		return configuration;
	}
	
	public void populateConfiguration(ConfigurationElement configuration, ExecutionResults results) {
		WindupResult result = WindupFactory.eINSTANCE.createWindupResult();
	    result.setExecutionResults(results);
	    configuration.getInputs().get(0).setWindupResult(result);
	    configuration.setTimestamp(createTimestamp());
	    
	    for (Iterator<Hint> iter = results.getHints().iterator(); iter.hasNext();) {
    		
	    		Hint wHint = iter.next();
	    	
	        	String path = wHint.getFile().getAbsolutePath();
	        	
	        	org.jboss.tools.windup.windup.Hint hint = WindupFactory.eINSTANCE.createHint();
	        	result.getIssues().add(hint);
	        	
	        	hint.setOriginalLineSource(wHint.getSourceSnippit());
	
	        	for (Quickfix fix : wHint.getQuickfixes()) {
	    			org.jboss.tools.windup.windup.QuickFix quickFix = WindupFactory.eINSTANCE.createQuickFix();
	        		quickFix.setQuickFixType(fix.getType().toString());
	        		quickFix.setSearchString(fix.getSearch());
	        		quickFix.setReplacementString(fix.getReplacement());
	        		quickFix.setNewLine(fix.getNewline());
	        		quickFix.setTransformationId(fix.getTransformationID());
	        		quickFix.setName(fix.getName());
	        		if (fix.getFile() != null) {
	        			quickFix.setFile(fix.getFile().getAbsolutePath());
	        		}
	        		else {
	        			// Fallback for quickfixes not assigned to file. Assume quickfix applies to file associated with the hint.
	        			quickFix.setFile(path);
	        		}
	        		hint.getQuickFixes().add(quickFix);
	        	}
	
	        	// TODO: I think we might want to change this to project relative for portability.
	        	hint.setFileAbsolutePath(wHint.getFile().getAbsolutePath());
	        	hint.setSeverity(wHint.getIssueCategory().getCategoryID().toUpperCase());
	        	hint.setRuleId(wHint.getRuleID());
	        	hint.setEffort(wHint.getEffort());
	        	
	        	hint.setTitle(wHint.getTitle());
	        	hint.setMessageOrDescription(wHint.getHint());
	        	hint.setLineNumber(wHint.getLineNumber());
	        	hint.setColumn(wHint.getColumn());
	        	hint.setLength(wHint.getLength());
	        	hint.setSourceSnippet(wHint.getSourceSnippit());
	        	
	        	for (Link wLink : wHint.getLinks()) {
	        		org.jboss.tools.windup.windup.Link link = WindupFactory.eINSTANCE.createLink();
	        		link.setDescription(wLink.getDescription());
	        		link.setUrl(wLink.getUrl());
	        		hint.getLinks().add(link);
	        	}
	    }
    
	    // TODO: Classifications
    
	    for (Classification wClassification : results.getClassifications()) {
	        	String path = wClassification.getFile().getAbsolutePath();
				
	        	org.jboss.tools.windup.windup.Classification classification = WindupFactory.eINSTANCE.createClassification();
	        	result.getIssues().add(classification);
	        	
	        	for (Quickfix fix : wClassification.getQuickfixes()) {
	    			org.jboss.tools.windup.windup.QuickFix quickFix = WindupFactory.eINSTANCE.createQuickFix();
	        		quickFix.setQuickFixType(fix.getType().toString());
	        		quickFix.setSearchString(fix.getSearch());
	        		quickFix.setReplacementString(fix.getReplacement());
	        		quickFix.setNewLine(fix.getNewline());
	        		quickFix.setTransformationId(fix.getTransformationID());
	        		quickFix.setName(fix.getName());
	        		if (fix.getFile() != null) {
	        			quickFix.setFile(fix.getFile().getAbsolutePath());
	        		}
	        		else {
	        			// Fallback for quickfixes not assigned to file. Assume quickfix applies to file associated with the hint.
	        			quickFix.setFile(path);
	        		}
	        		classification.getQuickFixes().add(quickFix);
	        	}
	
	        	classification.setFileAbsolutePath(wClassification.getFile().getAbsolutePath());
	        	classification.setSeverity(wClassification.getIssueCategory().getCategoryID().toUpperCase());
	        	classification.setRuleId(wClassification.getRuleID());
	        	classification.setEffort(wClassification.getEffort());
	        	classification.setTitle(wClassification.getClassification());
	        	
	        	for (Link wLink : wClassification.getLinks()) {
	        		org.jboss.tools.windup.windup.Link link = WindupFactory.eINSTANCE.createLink();
	        		link.setDescription(wLink.getDescription());
	        		link.setUrl(wLink.getUrl());
	        		classification.getLinks().add(link);
	        	}
    		}
    
	    //
	    linkReports(results, result.getIssues());
	    save();
	}
	
	private SimpleDateFormat getTimestampFormat() {
		return new SimpleDateFormat(TIMESTAMP_FORMAT);
	}
	
	public String createTimestamp() {
		return getTimestampFormat().format(new Date());
	}
	
	private void linkReports(ExecutionResults results, List<Issue> issues) {
		for (Issue issue : issues) {
			File file = new File(issue.getFileAbsolutePath());
			for (ReportLink link : results.getReportLinks()) {
				if (link.getInputFile().equals(file)) {
					File report = link.getReportFile();
					issue.setGeneratedReportLocation(report.getAbsolutePath());
					break;
				}
			}
		}
	}
}
