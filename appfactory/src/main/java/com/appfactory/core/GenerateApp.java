package com.appfactory.core;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appfactory.GitProcessing.GithubOperations;
import com.appfactory.exceptions.MyException;
import com.appfactory.ioprocessing.MultiIOprocessing;
import com.appfactory.ioprocessing.MultiZipProcessing;
import com.appfactory.platformpush.BindService;
import com.appfactory.platformpush.PushTheApp;
import com.appfactory.resources.Messages;
import com.appfactory.service.AccessData;

/**
 * File : GenerateApp.java Description : This class will have small small
 * implimentation of creating the dynamic app. Revision History : Version Date
 * Author Reason 0.1 14-June-2016 559296 Initial version
 */
@Service
public class GenerateApp {
	/**
	 * These two fields will be used for checking methods success or failure
	 * 
	 **/
	private static final String FAILURE = Messages.getString("ManagerController.Failed");
	private static final String SUCCESS = Messages.getString("ManagerController.Success");
	private static final Logger LOG = Logger.getLogger(GenerateApp.class.getName());

	@Autowired
	private AccessData accessdata;

	/**
	 * This method will create a base directory where all the operation will
	 * happen
	 * @throws MyException 
	 **/
	public String baseDirectory() throws MyException {
		MultiIOprocessing miop = new MultiIOprocessing();
		String parentdirectory = Messages.getString("Base_path") + System.currentTimeMillis();
		accessdata.setParentdirectory(parentdirectory);
		File createdir = miop.createFolder(parentdirectory, accessdata.getBlueprint().getServicename());
		if (!(createdir.exists())) {
			LOG.error("Base directory is not created");
			return FAILURE;
		}
		accessdata.setServicefolderpath(createdir);
		LOG.info("baseDirectory method is sucess");
		return cloneGit();
	}

	/**
	 * This method will clone the required codebase from the selected inputs
	 * @throws MyException 
	 **/
	private String cloneGit() throws MyException {
		ArrayList<String> returnarr = new ArrayList<String>();
		GithubOperations gito = new GithubOperations();
		returnarr = gito.cloneGit(accessdata.getServicefolderpath(), accessdata);
		if (returnarr.get(0).equalsIgnoreCase("error")) {
			LOG.error("Not able to get folders from git");
			return FAILURE;
		}
		accessdata.setDownloadfolders(returnarr);
		LOG.info("cloneGit method is sucess");
		return generateURL();
	}

	



	
	/**
	 * It will create config files with the urls
	 **/
	private String generateURL() {
		PushTheApp pushOperation = new PushTheApp();
		MultiIOprocessing miop = new MultiIOprocessing();

		ArrayList<String> urls = pushOperation.checkFortheplatform(accessdata.getBlueprint().getPlatform(),
				accessdata.getBlueprint().getServicename());
		accessdata.setRunningapp(urls.get(0));
		accessdata.setEndpoint(urls.get(1));
		accessdata.setHost(urls.get(2));
		miop.changeConfig(
				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
						+ accessdata.getBlueprint().getServicename(),
				accessdata.getBlueprint().getServicename(),
				accessdata.getBlueprint().getPrimaryservice().getProviders(), accessdata.getHookObj(),
				accessdata.getRunningapp());
		if (urls.isEmpty()) {
			return FAILURE;
		}
		LOG.info("generateURL method is sucess");
		return generateZip();
	}

	/**
	 * It will create zip file of the generated app.
	 **/
	private String generateZip() {
		String source = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename();
		String destzip = accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename() + Messages.getString("AppFactorySave.26");
		MultiZipProcessing mZip = new MultiZipProcessing();
		mZip.zipAction(source, destzip, accessdata.getBlueprint().getServicename(),
				Messages.getString("AppFactorySave.27"));

		LOG.info("generateZip method is sucess");
		return pushApp();
	}

	/**
	 * It will create push the app, and will start the app.
	 **/
	private String pushApp() {
		PushTheApp pushOperation = new PushTheApp();
		JSONObject appresponse = new JSONObject();
		appresponse.put(Messages.getString("AppFactorySave.accesstoken"), accessdata.getBlueprint().getAccesstoken());
		appresponse.put(Messages.getString("AppFactorySave.spaceguid"), accessdata.getBlueprint().getSpaceguid());
		String response = pushOperation.checkFortheplatform(accessdata.getBlueprint().getPlatform(),
				accessdata.getBlueprint().getServicename(), appresponse,
				accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
						+ accessdata.getBlueprint().getServicename() + Messages.getString("AppFactorySave.29"));
		if (response.contains("failed")) {
			return FAILURE;
		}

		String[] res = response.split(",");

		accessdata.setAppguid(res[0]);
		accessdata.setDomainguid(res[1]);
		LOG.info("pushApp method is sucess");
		return createMicro();
	}

	/**
	 * It will create the service instance, will get all the service,service
	 * plans,Bind service, create service instance,Restage,App env
	 * 
	 **/

	private String createMicro() {
		BindService bservice = new BindService();
		MultiIOprocessing mip = new MultiIOprocessing();
		PushTheApp pushOperation = new PushTheApp();
		String response;
		String platform = accessdata.getEndpoint();
		String token = accessdata.getBlueprint().getAccesstoken();
		if (!(platform.isEmpty())) {
			/**
			 * response = all the service list
			 */
			response = bservice.getAllServices(token, platform);
			accessdata.setEnv_json(mip.envoirmentjson(accessdata.getBlueprint().getPrimaryservice().getProviders()));
			JSONObject jobj = new JSONObject(response);
			JSONObject createservice = serviceobj(jobj);
			String serviceinstanceguid = bservice.createServiceInstance(token, accessdata.getBlueprint().getSpaceguid(),
					platform, createservice, jobj, accessdata.getAppguid(), accessdata.getBlueprint().getServicename());
			String createresponse[] = serviceinstanceguid.split(",");
			String serviceguid = createresponse[0];
			String serviceaappguid = createresponse[1];
			JSONObject bindobj = bindserviceobj();
			if (!(serviceinstanceguid.contains("Error"))) {
				bservice.bindServiceInstanceToApp(serviceguid, token, platform,
						accessdata.getBlueprint().getServicename(), accessdata.getAppguid(), jobj, bindobj,
						serviceaappguid);
				bservice.restageApp(accessdata.getAppguid(), accessdata.getBlueprint().getAccesstoken(), platform);
				bservice.getAppEnv(accessdata.getBlueprint().getAccesstoken(), accessdata.getAppguid(), platform);
				pushOperation.startApplication(accessdata.getBlueprint().getAccesstoken(), accessdata.getAppguid(),
						platform, accessdata.getDomainguid());
				/*
				 * bservice.restageApp(accessdata.getAppguid(),
				 * accessdata.getBlueprint().getAccesstoken(), platform);
				 */
			} else {
				return FAILURE;
			}
		} else {
			return FAILURE;
		}
		LOG.info("createMicro method is success");
		return gitPush();
	}

	/**
	 * It will push the app, to the devlopers github account.
	 **/
	private String gitPush() {
		GithubOperations gito = new GithubOperations();
		MultiIOprocessing miop = new MultiIOprocessing();
		File destination = miop.createFolder(accessdata.getParentdirectory(), Messages.getString("AppFactorySave.32"));
		File copydir = new File(accessdata.getParentdirectory() + Messages.getString("AppManager.frwdslash")
				+ accessdata.getBlueprint().getServicename());
		String applicationname = accessdata.getBlueprint().getServicename();
		String result = gito.push(applicationname, accessdata.getBlueprint().getDevlopergitusername(),
				accessdata.getBlueprint().getDevlopergitpassword(), accessdata.getBlueprint().getDevlopergiturl(),
				destination, copydir, Messages.getString("AppFactorySave.commituser"));
		if (!(result.equalsIgnoreCase(SUCCESS))) {
			return FAILURE;
		}
		LOG.info("gitPush method is sucess");
		return SUCCESS;
	}

	/**
	 * 
	 * @param jobj
	 * @return
	 */
	private JSONObject serviceobj(JSONObject jobj) {
		String serviceplan = (String) jobj.get("service_plan_guid");
		JSONObject token = new JSONObject();
		token.put("access_token", accessdata.getBlueprint().getAccesstoken());
		token.put("token_type", "bearer");
		token.put("refresh_token", "");
		token.put("expires_in", "");
		token.put("scope", "");
		token.put("jti", "");

		JSONObject createservice = new JSONObject();
		createservice.put("endpoint", accessdata.getEndpoint());
		createservice.put("organization_guid", accessdata.getBlueprint().getOrgguid());
		createservice.put("plan_id", serviceplan);
		createservice.put("space_guid", accessdata.getBlueprint().getSpaceguid());
		createservice.put("appname", accessdata.getBlueprint().getServicename() + "service");
		createservice.put("domain_guid", accessdata.getDomainguid());
		createservice.put("host", accessdata.getHost());
		createservice.put("environment_json", accessdata.getEnv_json());
		createservice.put("token", token);
		return createservice;
	}

	private JSONObject bindserviceobj() {
		JSONObject jobj = new JSONObject();
		if (!(accessdata.getBlueprint().getPrimaryservice().getProviders() == null)) {
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getFacebook() == null)) {
				jobj.put(Messages.getString("AppManager.facebook"), true);

			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getTwitter() == null)) {
				jobj.put(Messages.getString("AppManager.twitter"), true);
			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getGoogle() == null)) {
				jobj.put(Messages.getString("AppManager.google"), true);
			}
			if (!(accessdata.getBlueprint().getPrimaryservice().getProviders().getLinkedin() == null)) {
				jobj.put(Messages.getString("AppManager.linkedin"), true);
			}
			jobj.put("appname", accessdata.getBlueprint().getServicename() + "service");
			jobj.put("host", accessdata.getHost());
		} else {
			return jobj;
		}
		return jobj;
	}
}
