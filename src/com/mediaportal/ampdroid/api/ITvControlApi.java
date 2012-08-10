/*******************************************************************************
 * Copyright (c) 2011 Benjamin Gmeiner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Benjamin Gmeiner - Project Owner
 ******************************************************************************/
package com.mediaportal.ampdroid.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mediaportal.ampdroid.data.TvCard;
import com.mediaportal.ampdroid.data.TvCardDetails;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvChannelDetails;
import com.mediaportal.ampdroid.data.TvChannelGroup;
import com.mediaportal.ampdroid.data.TvProgram;
import com.mediaportal.ampdroid.data.TvRecording;
import com.mediaportal.ampdroid.data.TvRtspClient;
import com.mediaportal.ampdroid.data.TvSchedule;
import com.mediaportal.ampdroid.data.TvUser;

public interface ITvControlApi extends IApiInterface {
	boolean TestConnectionToTVService();

	void AddSchedule(int channelId, String title, Date startTime, Date endTime,
			int scheduleType);

	String SwitchTVServerToChannelAndGetStreamingUrl(int channelId);

	String SwitchTVServerToChannelAndGetTimeshiftFilename(int channelId);

	void CancelCurrentTimeShifting();

	List<TvChannel> GetChannels(int groupId);

	List<TvRecording> GetRecordings();

	List<TvSchedule> GetSchedules();

	TvChannelDetails GetChannelById(int _channelId);

	TvProgram GetProgramById(int programId);

	List<TvProgram> GetProgramsForChannel(int channelId, Date startTime,
			Date endTime);

	boolean GetProgramIsScheduledOnChannel(int channelId, int programId);

	List<TvProgram> SearchPrograms(String searchTerm);

	ArrayList<TvChannelGroup> GetGroups();

	void CancelSchedule(int programId);

	List<TvCardDetails> GetCards();

	List<TvCard> GetActiveCards();

	List<TvRtspClient> GetStreamingClients();

	List<TvUser> GetActiveUsers();

	TvProgram GetCurrentProgramOnChannel(int channelId);

	String ReadSettingFromDatabase(String tagName);

	void WriteSettingToDatabase(String tagName, String value);

   List<TvChannel> GetChannels(int _groupId, int _startIndex, int _endIndex);

   int GetChannelsCount(int _groupId);

   List<TvChannelDetails> GetChannelsDetails(int groupId);

   List<TvChannelDetails> GetChannelsDetails(int groupId, int startIndex, int endIndex);

}
