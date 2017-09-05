package com.merecedes.data.entity;

public class MercedesQueryEntity {
	private String variant;
	private String cityName;

	private int kilometers;
	private int year;
	private int owners;

	public int getKilometers() {
		return kilometers;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getOwners() {
		return owners;
	}

	public void setOwners(int owners) {
		this.owners = owners;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	@Override
	public String toString() {
		return "MercedesQueryEntity [variant=" + variant + ", cityName=" + cityName + ", kilometers=" + kilometers
				+ ", year=" + year + ", owners=" + owners + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + kilometers;
		result = prime * result + owners;
		result = prime * result + ((variant == null) ? 0 : variant.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MercedesQueryEntity other = (MercedesQueryEntity) obj;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (kilometers != other.kilometers)
			return false;
		if (owners != other.owners)
			return false;
		if (variant == null) {
			if (other.variant != null)
				return false;
		} else if (!variant.equals(other.variant))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}
