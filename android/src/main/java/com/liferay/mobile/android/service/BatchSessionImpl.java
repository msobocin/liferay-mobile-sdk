/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.android.service;

import android.os.AsyncTask;

import com.liferay.mobile.android.auth.Authentication;
import com.liferay.mobile.android.http.HttpUtil;
import com.liferay.mobile.android.task.ServiceAsyncTask;
import com.liferay.mobile.android.task.callback.BatchAsyncTaskCallback;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class BatchSessionImpl extends SessionImpl {

	public BatchSessionImpl(Session session) {
		super(session);
	}

	public BatchSessionImpl(String server) {
		super(server);
	}

	public BatchSessionImpl(String server, Authentication authentication) {
		super(server, authentication);
	}

	public BatchSessionImpl(
		String server, Authentication authentication,
		BatchAsyncTaskCallback callback) {

		super(server, authentication, callback);
	}

	public BatchSessionImpl(
		String server, Authentication authentication, int connectionTimeout,
		BatchAsyncTaskCallback callback) {

		super(server, authentication, connectionTimeout, callback);
	}

	public BatchSessionImpl(String server, BatchAsyncTaskCallback callback) {
		super(server, callback);
	}

	public JSONArray invoke() throws Exception {
		if (_commands.size() == 0) {
			return null;
		}

		JSONArray commands = new JSONArray(_commands);

		try {
			if (callback != null) {
				ServiceAsyncTask task = new ServiceAsyncTask(this, callback);
				task.execute(commands);

				return null;
			}
			else {
				return HttpUtil.post(this, commands);
			}
		}
		finally {
			_commands = new ArrayList<JSONObject>();
		}
	}

	@Override
	public JSONArray invoke(JSONObject command) throws Exception {
		_commands.add(command);

		return null;
	}

	public void setCallback(BatchAsyncTaskCallback callback) {
		this.callback = callback;
	}

	@Override
	public AsyncTask upload(JSONObject command) throws Exception {
		throw new IllegalStateException("Can't batch upload requests");
	}

	private ArrayList<JSONObject> _commands = new ArrayList<JSONObject>();

}