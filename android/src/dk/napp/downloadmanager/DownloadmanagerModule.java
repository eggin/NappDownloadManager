/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package dk.napp.downloadmanager;

import java.util.ArrayList;
import java.util.EnumSet;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;


@Kroll.module(name="Downloadmanager", id="dk.napp.downloadmanager")
public class DownloadmanagerModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "DownloaderModule";
	private static final boolean DBG = TiConfig.LOGD;

	private ProgressiveDownloader downloader;
	private DownloadmanagerModule self;
	
	public static final String EVENT_PROGRESS = "progress";
	public static final String EVENT_OVERALL_PROGRESS = "overallprogress";
	public static final String EVENT_PAUSED = "paused";
	public static final String EVENT_FAILED = "failed";
	public static final String EVENT_COMPLETED = "completed";
	public static final String EVENT_CANCELLED = "cancelled";
	public static final String EVENT_STARTED = "started";
	public static final String EVENT_BATCHPAUSED = "batchpaused";
	public static final String EVENT_BATCHFAILED = "batchfailed";
	public static final String EVENT_BATCHCOMPLETED = "batchcompleted";
	public static final String EVENT_BATCHCANCELLED = "batchcancelled";
	
	@Kroll.constant public static final int NETWORK_TYPE_WIFI = 0;
	@Kroll.constant public static final int NETWORK_TYPE_MOBILE = 1;
	@Kroll.constant public static final int NETWORK_TYPE_ANY = 2;

	@Kroll.constant public static final int DOWNLOAD_PRIORITY_LOW = 1;
	@Kroll.constant public static final int DOWNLOAD_PRIORITY_NORMAL = 2;
	@Kroll.constant public static final int DOWNLOAD_PRIORITY_HIGH = 3;
	
	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	
	public DownloadmanagerModule() {
		super("Downloader");
		downloader = new ProgressiveDownloader(TiApplication.getAppRootOrCurrentActivity());
		downloader.setMaximumSimultaneousDownloads(4);
		downloader.DownloadProgress.addListener(new ProgressListener());
		downloader.DownloadPaused.addListener(new PausedListener());
		downloader.DownloadFailed.addListener(new FailedListener());
		downloader.DownloadCompleted.addListener(new CompletedListener());
		downloader.DownloadCancelled.addListener(new CancelledListener());
		downloader.DownloadStarted.addListener(new StartedListener());
		downloader.DownloadBatchPaused.addListener(new BatchPausedListener());
		downloader.DownloadBatchFailed.addListener(new BatchFailedListener());
		downloader.DownloadBatchCompleted.addListener(new BatchCompletedListener());
		downloader.DownloadBatchCancelled.addListener(new BatchCancelledListener());
		
		this.self = this;
	}
	
	class ProgressListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			
			if(self.hasListeners(EVENT_PROGRESS)) {
				KrollDict dict = createDict(event.getDownloadInformation());
				self.fireEvent(EVENT_PROGRESS, dict);
			}
			
			// overall progress
			if(self.hasListeners(EVENT_OVERALL_PROGRESS)) {
				int downloadedBytes = 0;
				int totalBytes = 0;
				int totalBps = 0;
				int count = 0;
				int averageBps = 0;
				int procentage = 0;
				
				// get all items from the queue
				for (DownloadInformation di : downloader.getDownloadInformation()) {
					downloadedBytes += (int) di.getAvailableLength();
					totalBytes += (int) di.getLength();
					totalBps += di.getLastDownloadBitsPerSecond();
					count++;
				}
				
				// calc average and progress procentage
				if(count > 0){
					averageBps = totalBps / count;
				}
				
				if(totalBytes > 0 && downloadedBytes > 0){
					procentage = downloadedBytes * 100 / totalBytes;
				}
				
				KrollDict overallDict = new KrollDict();
				overallDict.put("downloadedBytes", downloadedBytes);
				overallDict.put("totalBytes", totalBytes);
				overallDict.put("procentage", procentage);
				overallDict.put("averageBps", averageBps);
				overallDict.put("bps", totalBps);
				
				self.fireEvent(EVENT_OVERALL_PROGRESS, overallDict);
			}
			
		}
	}
	class PausedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			if(self.hasListeners(EVENT_PAUSED)) {
				KrollDict dict = createDict(event.getDownloadInformation());
				self.fireEvent(EVENT_PAUSED, dict);
			}
		}
	}
	class FailedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			if(self.hasListeners(EVENT_FAILED)) {
				KrollDict dict = createDict(event.getDownloadInformation());
				self.fireEvent(EVENT_FAILED, dict);
			}
		}
	}
	class CompletedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			if(self.hasListeners(EVENT_COMPLETED)) {
				KrollDict dict = createDict(event.getDownloadInformation());			
				self.fireEvent(EVENT_COMPLETED, dict);
			}
		}
	}
	class CancelledListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			if(self.hasListeners(EVENT_CANCELLED)) {
				KrollDict dict = createDict(event.getDownloadInformation());
				self.fireEvent(EVENT_CANCELLED, dict);
			}
		}
	}

	class StartedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			if(self.hasListeners(EVENT_STARTED)) {
				KrollDict dict = createDict(event.getDownloadInformation());
				dict.put("reason", event.getDownloadInformation().getMessage());
				self.fireEvent(EVENT_STARTED, dict);
			}
		}
	}
	
	class BatchPausedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			//Log.d(LCAT, "Download Batch Paused ");
		}
	}
	class BatchFailedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			//Log.d(LCAT, "Download Batch Failed ");
		}
	}
	class BatchCompletedListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			//Log.d(LCAT, "Download Batch Completed ");
		}
	}
	class BatchCancelledListener implements IListener<DownloadEvent>
	{
		@Override
		public void handleEvent(DownloadEvent event) {
			//Log.d(LCAT, "Download Batch Cancelled ");
		}
	}
	

	@Kroll.getProperty
	public int getMaximumSimultaneousDownloads() {
		return downloader.getMaximumSimultaneousDownloads();
	}

	@Kroll.setProperty @Kroll.method
	public void setMaximumSimultaneousDownloads(int count) {
		downloader.setMaximumSimultaneousDownloads(count);
	}	
	
	@Kroll.getProperty
	public int getPermittedNetworkTypes() {
		EnumSet<NetworkTypes> types = downloader.getPermittedNetworkTypes();
		if (types.containsAll(NetworkTypes.Any)) {
			return NETWORK_TYPE_ANY;
		} else if (types.containsAll(NetworkTypes.Mobile)) {
			return NETWORK_TYPE_MOBILE;
		} else if (types.contains(NetworkTypes.Wireless80211)) {
			return NETWORK_TYPE_WIFI;
		}
		
		return -1;
	}

	@Kroll.setProperty @Kroll.method
	public void setPermittedNetworkTypes(int type) {
		if (type == NETWORK_TYPE_ANY) {
			downloader.setPermittedNetworkTypes(NetworkTypes.Any);
		} else if (type == NETWORK_TYPE_WIFI) {
			downloader.setPermittedNetworkTypes(EnumSet.of(NetworkTypes.Wireless80211));
		} else if (type == NETWORK_TYPE_MOBILE) {
			downloader.setPermittedNetworkTypes(NetworkTypes.Mobile);
		}
	}
	
	// Methods
	@Kroll.method
	public void addDownload(KrollDict dict) {
		DownloadRequest request = new DownloadRequest();
		request.setUrl(TiConvert.toString(dict, "url"));
		request.setName(TiConvert.toString(dict, "name"));
		request.setLocale("eng");
		request.setFileName(TiConvert.toString(dict, "filePath"));
		
		int priority = 0;
		if (dict.containsKey("priority")) {
			priority = TiConvert.toInt(dict, "priority");
		}
		switch(priority) {
			case 1:  request.setDownloadPriority(DownloadPriority.Low); break;
			case 3:  request.setDownloadPriority(DownloadPriority.High); break;
			default: request.setDownloadPriority(DownloadPriority.Normal); break;
		}
		
		downloader.download(request);
	}
	
	@Kroll.method
	public void pauseAll() {
		downloader.pause();
	}
	
	@Kroll.method
	public void pauseItem(String url) {
		downloader.pause(url);
	}
	
	@Kroll.method
	public void resumeAll() {
		downloader.resume();
	}
	
	@Kroll.method
	public void resumeItem(String url) {
		downloader.resume(url);
	}
	
	@Kroll.method
	public void cancelItem(String url) {
		downloader.cancel(url);
	}
	
	@Kroll.method
	public void stopDownloader() {
		downloader.stop();
	}
	
	@Kroll.method
	public void restartDownloader() {
		downloader.start();		
	}
	
	@Kroll.method
	public void deleteItem(String url) {
		downloader.delete(url);
	}
	
	@Kroll.method
	public KrollDict getDownloadInfo(String url) {
		
		DownloadInformation di = downloader.getDownloadInformation(url);
		if (di == null)
		{
			return null;
		}
		
		return createDict(di);
	}
	
	@Kroll.method
	public KrollDict[] getAllDownloadInfo() {
		ArrayList<KrollDict> list = new ArrayList<KrollDict>();
		for (DownloadInformation di : downloader.getDownloadInformation()) {
			list.add(createDict(di));
		}
		
		KrollDict[] array = new KrollDict[0];
				
		return list.toArray(array);
	}
	
	@Kroll.method
	public void deleteQueue() {
		
		// stop the downloader
		downloader.stop();
		
		for (DownloadInformation di : downloader.getDownloadInformation()) {
			downloader.delete(di.getUrl());
		}
		
		// restart it
		downloader.start();
	}

	private KrollDict createDict(DownloadInformation di) {
		KrollDict dict = new KrollDict();
		dict.put("name", di.getName());
		dict.put("url", di.getUrl());
		dict.put("downloadedBytes", di.getAvailableLength());
		dict.put("totalBytes", di.getLength());
		dict.put("bps", di.getLastDownloadBitsPerSecond());
		dict.put("filePath", di.getFilePath());
		dict.put("createdDate", di.getCreationUtc());
		dict.put("priority", di.getDownloadPriority().getValue());
		return dict;
	}

}

