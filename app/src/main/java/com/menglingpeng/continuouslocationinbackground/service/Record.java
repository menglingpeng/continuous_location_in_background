package com.menglingpeng.continuouslocationinbackground.service;

import com.amap.api.location.AMapLocation;
import com.menglingpeng.continuouslocationinbackground.RecordLocation;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class Record extends RealmObject {


    public int id;
    public int recordType;
    public String distance;
    public String duration;
    public String speed;//这里是averageSpeed
    public String pathLine;//所有点的Gson字符串。
    private AMapLocation mStartPoint;
    private AMapLocation mEndPoint;
    public String startPoint;//起始点的重要字段字符串，对应RecordLocation的LocationStr
    public String endPoint;//结束点的重要字段字符串，对应RecordLocation的LocationStr
    public String date;//起始点对应Timestamp的dateStr

    public List<RecordLocation> mPathLocationList = new ArrayList<>();
    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPathLine() {
        return pathLine;
    }

    public void setPathLine(String pathLine) {
        this.pathLine = pathLine;
    }

    public AMapLocation getmStartPoint() {
        return mStartPoint;
    }

    public void setmStartPoint(AMapLocation mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public AMapLocation getmEndPoint() {
        return mEndPoint;
    }

    public void setmEndPoint(AMapLocation mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RecordLocation> getmPathLocationList() {
        return mPathLocationList;
    }

    public void setmPathLocationList(List<RecordLocation> mPathLocationList) {
        this.mPathLocationList = mPathLocationList;
    }
}
