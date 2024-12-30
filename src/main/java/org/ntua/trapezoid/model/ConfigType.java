/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ntua.trapezoid.model;

/**
 *
 * @author FITHIS
 */
public class ConfigType {

    public static class ScatterBubble2DConfigType {

        public String graphType = "ScatterBubble2D";
        // public boolean showTransition = false;
        // public String colorBy = "grouping";
        // public String theme = "ggplot";
        // public String plotStyle = "ggplot";
        public String colorScheme = "Basic";
        public String blindnessType = "monochromatic";
        public String[] xAxis = new String[]{"Ts"};
        public String xAxisTitle = "Ts";
        public String[] yAxis = new String[]{"Fr"};
        public String yAxisTitle = "Fr";
        public String[] zAxis = new String[]{"Occurences"};

    }

    public static class HeatMapConfigType {

        public String graphType = "Heatmap";
        public String heatmapCellBox = "false";
        public String showContourLevel = "false";
        public String showSampleNames = "false";
        public String showVariableNames = "false";
    }

}
