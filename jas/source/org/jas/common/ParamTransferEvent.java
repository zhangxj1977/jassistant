package org.jas.common;

import java.util.EventObject;

/**
 *
 *
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class ParamTransferEvent extends EventObject {

	/**
	 * 結果オブジェクト
	 */
	private Object param = null;

	/**
	 * 区分フラグ
	 */
	private int opFlag = 0;

	public ParamTransferEvent(Object source) {
		super(source);
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public int getOpFlag() {
		return opFlag;
	}

	public void setOpFlag(int opFlag) {
		this.opFlag = opFlag;
	}
}