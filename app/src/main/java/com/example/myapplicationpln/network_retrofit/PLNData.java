package com.example.myapplicationpln.network_retrofit;

public class PLNData {
    private double MeterValue ;
    private double ScoreClassification ;
    private double ScoreIdentification ;

    public double getMeterValue() {
        return MeterValue;
    }

    public void setMeterValue(double meterValue) {
        MeterValue = meterValue;
    }

    public double getScoreClassification() {
        return ScoreClassification;
    }

    public void setScoreClassification(double scoreClassification) {
        ScoreClassification = scoreClassification;
    }

    public double getScoreIdentification() {
        return ScoreIdentification;
    }

    public void setScoreIdentification(double scoreIdentification) {
        ScoreIdentification = scoreIdentification;
    }
}
