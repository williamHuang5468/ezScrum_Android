package ntut.mobile.ezscrum.view.burndownchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ntut.mobile.ezscrum.controller.burndownchart.BurndownChartManager;
import ntut.mobile.ezscrum.internal.ChartTag;
import ntut.mobile.ezscrum.internal.ChartType;
import ntut.mobile.ezscrum.model.BurndownChartObject;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class BurndownChart extends AbstractChart {

	private List<BurndownChartObject> mData;
	private int mSprintID;
	private String mProjectID;
	private Context mContext;
	private ChartType mType;

	public BurndownChart(Context context, ChartType type, String projectID, String sprintID) {
		this.mContext = context;
		this.mProjectID = projectID;
		this.mSprintID = Integer.valueOf(sprintID);
		this.mType = type;
		mData = getBurnDownChartData();
	}

	
	/**
	 * 取得burndown chart資料
	 * @param mProjectID
	 * @param userName
	 * @param password
	 * @param mSprintID
	 * @return
	 */
	private List<BurndownChartObject> getBurnDownChartData() {
		BurndownChartManager burndownChartManager = new BurndownChartManager();
		return burndownChartManager.getStoryBurndownChart(mProjectID, String.valueOf(mSprintID));		
	}

	public View getBurndownChartView() {
		/* 設定圖表的折線 */
		String[] lineTitles = new String[] { ChartTag.IDEAL, ChartTag.CURRENT };
		int[] lineColors = new int[] { Color.rgb(153, 187, 232), Color.RED };
		PointStyle[] lineStyles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.CIRCLE };
		List<double[]> values = new ArrayList<double[]>();
		List<Date[]> dates = new ArrayList<Date[]>();

		/* Current Line Size */
		int currentLineSize = 0;
		for (int i=0; i< mData.size(); i++) {
			BurndownChartObject storyBurnDownChartObject = (BurndownChartObject) mData.get(i);
    		if (!storyBurnDownChartObject.getPoint().equals(""))
    			currentLineSize++;
    		else
    			break;
		}
		

		// 設定Current line 與 Ideal line的Value與Date大小 
		double maxCurrentValue = 0;
		double[] currentValue = new double[currentLineSize];
		Date[] currentDate = new Date[currentLineSize];
		double[] idealValue = new double[mData.size()];
		Date[] idealDate = new Date[mData.size()];

    	/* 設定Current line的數值(Value, Date) */
    	for(int i = 0 ; i < currentLineSize ; i++) {
         
    		BurndownChartObject storyBurnDownChartObject = (BurndownChartObject)mData.get(i);
          
    		// 取得點數或時數資料
    		currentValue[i] = Double.parseDouble(storyBurnDownChartObject.getPoint());
    		currentDate[i] = storyBurnDownChartObject.getDate();       
    		// 取最大值
    		if (maxCurrentValue < currentValue[i])
    			maxCurrentValue = currentValue[i];
    	}
    	
    	/* 設定Ideal line的數值(Value, Date) */
    	for(int i = 0 ; i < mData.size() ; i++) {
          
    		BurndownChartObject storyBurnDownChartObject = (BurndownChartObject)mData.get(i);
    		
    		// 計算ideal點數
    		double totalPoint = 0;
    		if (!((BurndownChartObject) mData.get(0)).getPoint().equals("")) {
    			totalPoint = Double.parseDouble(((BurndownChartObject)mData.get(0)).getPoint());
    		} else {
    			totalPoint = 0;
    		}
//    		double delta = totalPoint / (data.size() - 1);
//    		idealValue[i] = (totalPoint - ( i * delta));
    		double delta = Math.round( totalPoint / (mData.size()-1) );
    		idealValue[i] = (totalPoint - ( i * delta));
    		// 取得日期資料
    		idealDate[i] = storyBurnDownChartObject.getDate();
//    		System.out.println("idealDate " + i + " = " + idealDate[i]);
//    		System.out.println("idealValue " + i + " = " + idealValue[i]);
    	}

    	/* List中放入ideal line */
		values.add(idealValue);
		dates.add(idealDate);
		/* List中放入current line */
		values.add(currentValue);
		dates.add(currentDate);

		// 畫線
		XYMultipleSeriesRenderer renderer = buildRenderer(lineColors,
				lineStyles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setLineWidth(3);
		}
		
      	// 設定圖表的X軸與Y軸label, chart title
      	String unit = "";
      	String chartTitle = "";
		if ( mType.equals(ChartType.STORY)) {
      		unit = ChartTag.STORY_POINT;
      		chartTitle = "Story Burndown Chart";
		} else if ( mType.equals(ChartType.TASK)) {
			unit = ChartTag.HOURS;
			chartTitle = "Task Burndown Chart";
		}
		
		setChartSettings(renderer, chartTitle, "Date", unit, idealDate[0].getTime(),
				idealDate[idealDate.length - 1].getTime(), 0,
				maxCurrentValue*1.1, Color.DKGRAY, Color.DKGRAY);
		
		renderer.setMarginsColor(Color.WHITE);
		renderer.setXLabels(idealDate.length);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPointSize(5);
		renderer.setLabelsTextSize(10f);
		
		// 設定縮放大小
		renderer.setPanEnabled(false, false);
		renderer.setZoomRate(0.2f);
		renderer.setZoomEnabled(false, false);
		
		return ChartFactory.getTimeChartView(mContext, buildDateDataset(lineTitles, dates, values), renderer, "MM/dd");
	}
}