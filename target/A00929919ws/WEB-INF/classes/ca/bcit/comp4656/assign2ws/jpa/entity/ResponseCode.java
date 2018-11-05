package ca.bcit.comp4656.assign2ws.jpa.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseCode {

	private String code;
	private String desc;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "ResponseCode [code=" + code + ", desc=" + desc + "]";
	}
}
