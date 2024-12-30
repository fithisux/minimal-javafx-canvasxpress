package org.ntua.trapezoid.ui;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.controlsfx.dialog.ExceptionDialog;

import com.google.gson.Gson;

import org.ntua.trapezoid.model.PlotType;
import org.ntua.trapezoid.model.YType;
import org.ntua.trapezoid.model.ZType;
import org.ntua.trapezoid.model.ConfigType.HeatMapConfigType;
import org.ntua.trapezoid.model.ConfigType.ScatterBubble2DConfigType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainApp {

	@FXML
	WebView heatmapWebView;

	@FXML
	WebView bubbleWebView;
	
	WebEngine heatmapWebEngine;
	WebEngine bubbleWebEngine;
	
	
	public void loadCharts() {
		heatmapWebEngine.reload();
		bubbleWebEngine.reload();
	}

	/**
	 * Initializes the root layout.
	 */
	@FXML
	public void initialize() {
		System.out.println("Initialized");
		
		heatmapWebEngine = this.heatmapWebView.getEngine();
		heatmapWebEngine.getLoadWorker().stateProperty()
				.addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						System.out.println("OK1 "+newState);
						if (newState == State.SUCCEEDED) {
							loadHeatmap();
						}
					}
				});
		bubbleWebEngine = this.bubbleWebView.getEngine();
		bubbleWebEngine.getLoadWorker().stateProperty()
				.addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						System.out.println("OK2 "+newState);
						if (newState == State.SUCCEEDED) {
							loadBubbles();
						}
					}
				});

		heatmapWebEngine.load(getClass().getClassLoader().getResource("newcanvas.html").toExternalForm());
		bubbleWebEngine.load(getClass().getClassLoader().getResource("newcanvas.html").toExternalForm());
	}


	public static class HistoPoint {

		String title;
		Double[] pp = new Double[3];
	}

	private void loadBubbles() {

		Gson gson = new Gson();
		var xbins = 10;
		var ybins = 12;

		
		int varIndex = 0;
		var points = new ArrayList<HistoPoint>();

		for (int i = 0; i <xbins; i++) {
			for (int j = 0; j < ybins; j++) {
				var datum_z = 3.0*i+j;
				var datum_x = i/9.0;
				var datum_y = 2 * i/11.0;
				HistoPoint point = new HistoPoint();
				point.title = "Variable" + (++varIndex);
				point.pp[0] = datum_x;
				point.pp[1] = datum_y;
				point.pp[2] = datum_z;

				if (point.pp[2] > 0) {
					points.add(point);
				}
			}
		}

		YType y = new YType();
		String[] grouping = new String[points.size()];
		int index = 0;
		y.vars = new String[points.size()];
		y.data = new Double[points.size()][3];
		for (HistoPoint p : points) {
			y.vars[index] = p.title;
			y.data[index] = p.pp;
			grouping[index] = "ZZZZ";
			index++;
		}
		y.smps[0] = "Ts";
		y.smps[1] = "Fr";
		y.smps[2] = "Occurences";
		PlotType ptype = new PlotType();
		ptype.y = y;
		ptype.z = new ZType();
		ptype.z.grouping = grouping;

		String plot_data = gson.toJson(ptype);
		String config_data = gson.toJson(new ScatterBubble2DConfigType());
		String cmd = "var cv=new CanvasXpress('mycanvas'," + plot_data + "," + config_data + ", false);";
		bubbleWebEngine.executeScript(cmd);
	}

	private void loadHeatmap() {

		var xbins = 10;
		var ybins = 12;
		YType y = new YType();
		y.vars = new String[xbins];
		y.smps = new String[ybins];
		y.data = new Double[xbins][ybins];


		for (int i = 0; i <xbins; i++) {
			for (int j = 0; j < ybins; j++) {
				var datum_z = 3.0*i+j;
				var datum_x = i/9.0;
				var datum_y = 2 * i/11.0;
				y.data[i][y.data[i].length - 1 - j] = datum_z;
				y.vars[i] = "Ts(" + String.format("%.2f", datum_x) + ")";
				y.smps[y.data[i].length - 1 - j] = "Fr(" + String.format("%.2f", datum_y) + ")";
			}
		}

		PlotType ptype = new PlotType();
		ptype.y = y;

		Gson gson = new Gson();
		String plot_data = gson.toJson(ptype);
		String config_data = gson.toJson(new HeatMapConfigType());
		String cmd = "var cv=new CanvasXpress('mycanvas'," + plot_data + "," + config_data + ");";
		heatmapWebEngine.executeScript(cmd);
	}
}