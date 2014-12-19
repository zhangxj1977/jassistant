package org.jas.common;

import java.util.EventListener;

/**
 *
 *
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public interface ParamTransferListener extends EventListener {

	/**
	 * 結果返すイベント
	 *
	 */
	public void paramTransfered(ParamTransferEvent pe);

}