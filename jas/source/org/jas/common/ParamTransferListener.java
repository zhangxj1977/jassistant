package org.jas.common;

import java.util.EventListener;

/**
 *
 *
 *
 *
 * @author ���@�w�R
 * @version 1.0
 */

public interface ParamTransferListener extends EventListener {

	/**
	 * ���ʕԂ��C�x���g
	 *
	 */
	public void paramTransfered(ParamTransferEvent pe);

}