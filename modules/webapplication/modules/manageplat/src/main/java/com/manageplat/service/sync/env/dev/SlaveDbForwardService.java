package com.manageplat.service.sync.env.dev;

import com.manageplat.service.sync.forward.PKeyForwardService;

public class SlaveDbForwardService extends PKeyForwardService {

	@Override
	protected void afterLogin(){
		super.createPortForwardLocal();
	}
}
