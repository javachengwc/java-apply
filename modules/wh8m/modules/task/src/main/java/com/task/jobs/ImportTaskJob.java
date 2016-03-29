package com.task.jobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.task.util.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 导入任务日志
 */
public class ImportTaskJob extends QuartzJobBean {

	private static final String LOG_FILE = "/home/log/task.log.";

    private static Logger logger = LoggerFactory.getLogger(ImportTaskJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			analyzeLogFile();
		} catch (Exception e) {
			logger.error("import task record error", e);
		}
	}

	private void analyzeLogFile() throws FileNotFoundException, IOException {
		InputStream in = new FileInputStream(LOG_FILE + getYesterday());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<Integer, Map<Integer, TaskStats>>> developmentMap = new HashMap<String, Map<Integer, Map<Integer, TaskStats>>>();
		map.put("total", new HashMap<String, Integer>());
		map.put("today", new HashMap<String, Integer>());
		try {
			String line = null;
			String label1 = "finished task";
			while ((line = reader.readLine()) != null) {
				int index = line.indexOf(label1);
				if (index > 0) {
					String taskId = line.substring(index + label1.length() + 1,
							index + label1.length() + 8).trim();
					Map<String, Integer> tmpMap = map.get("total");
					incrFinishedNumber(taskId, tmpMap);
					if (line.indexOf("today") > 0) {
						tmpMap = map.get("today");
						incrFinishedNumber(taskId, tmpMap);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		logger.info(map.toString());
		saveTaskTimeRecord(map);
		saveTaskRecord(developmentMap);
	}

	private void incrFinishedNumber(String taskId, Map<String, Integer> tmpMap) {
		if (tmpMap.containsKey(taskId)) {
			int num = tmpMap.get(taskId) + 1;
			tmpMap.put(taskId, num);
		} else {
			tmpMap.put(taskId, 1);
		}
	}

	private String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(cal.getTime());
	}

	private void saveTaskTimeRecord(Map<String, Map<String, Integer>> map) {
		Connection conn = null;
		try {
			conn = SpringContextUtils.getDataSource().getConnection();
			conn.setAutoCommit(true);
			Timestamp time = Timestamp.valueOf(getYesterday() + " 0:0:0");
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement("insert into task_record(id,num,time,type) values(?,?,?,?)");
				for (Map.Entry<String, Integer> entry : map.get("total")
						.entrySet()) {
					pstmt.setString(1, entry.getKey());
					pstmt.setInt(2, entry.getValue());
					pstmt.setTimestamp(3, time);
					pstmt.setString(4, "total");
					pstmt.addBatch();
				}
				for (Map.Entry<String, Integer> entry : map.get("today")
						.entrySet()) {
					pstmt.setString(1, entry.getKey());
					pstmt.setInt(2, entry.getValue());
					pstmt.setTimestamp(3, time);
					pstmt.setString(4, "today");
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						logger.error("error", e);
					}
				}
			}
		} catch (SQLException e) {
            logger.error("error", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
                    logger.error("error", e);
				}
			}
		}
	}

	private void saveTaskRecord(Map<String, Map<Integer, Map<Integer, TaskStats>>> developmentMap) {
		List<TaskStats> list = new ArrayList<TaskStats>();
		for (Map<Integer, Map<Integer, TaskStats>> stageStats : developmentMap.values())
        {
			for (Map<Integer, TaskStats> gradeStats : stageStats.values()) {
				list.addAll(gradeStats.values());
			}
		}
		Connection conn = null;
		try {
			conn = SpringContextUtils.getDataSource().getConnection();
			conn.setAutoCommit(true);
			Timestamp time = Timestamp.valueOf(getYesterday() + " 0:0:0");
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement("insert into app_task_record(id,stage,grade,count,time) values(?,?,?,?,?)");
				for (TaskStats developmentTaskStats : list) {
					pstmt.setString(1, developmentTaskStats.symbol);
					pstmt.setInt(2, developmentTaskStats.stage);
					pstmt.setInt(3, developmentTaskStats.memberGrade);
					pstmt.setInt(4, developmentTaskStats.count);
					pstmt.setTimestamp(5, time);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("error", e);
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						logger.error("error", e);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("error", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("error", e);
				}
			}
		}
	}
    public static class TaskStats {
        String symbol;
        int stage;
        int memberGrade;
        int count;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public int getMemberGrade() {
            return memberGrade;
        }

        public void setMemberGrade(int memberGrade) {
            this.memberGrade = memberGrade;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }
}
