package com.hmif.model;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by gilang on 06/07/2015.
 */
public class Himpunan {

	private String namaSingkat;
	private String nama;
	private String facebookId;
	private String twitterId;
	private String lineId;
	private String alamat;
	private List<String> listHeader;
	private List<String> listInfo;

	public Himpunan(){}

	public Himpunan(String namaSingkat, String nama, String facebookId, String twitterId, String
			lineId, String alamat, @Nullable List<String> listHeader, List<String> listInfo){
		this.namaSingkat = namaSingkat;
		this.nama = nama;
		this.facebookId = facebookId;
		this.twitterId = twitterId;
		this.lineId = lineId;
		this.alamat = alamat;
		this.listHeader = listHeader;
		this.listInfo = listInfo;
	}

	public String getNamaSingkat() {
		return namaSingkat;
	}

	public void setNamaSingkat(String namaSingkat) {
		this.namaSingkat = namaSingkat;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public List<String> getListHeader() {
		return listHeader;
	}

	public void setListHeader(List<String> listHeader) {
		this.listHeader = listHeader;
	}

	public List<String> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<String> listInfo) {
		this.listInfo = listInfo;
	}
}
