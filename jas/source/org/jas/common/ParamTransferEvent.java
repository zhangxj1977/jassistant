package org.jas.common;

import java.util.EventObject;

/**
 *
 *
 *
 *
 * @author ���@�w�R
 * @version 1.0
 */

public class ParamTransferEvent extends EventObject {

	/**
	 * ���ʃI�u�W�F�N�g
	 */
	private Object param = null;

	/**
	 * �敪�t���O
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