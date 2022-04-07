package com.manageplat.service.sync.env.dev;

import com.manageplat.service.sync.forward.PwdForwardService;

public class DbForwardService extends PwdForwardService {
	@Override
	protected void afterLogin(){
		super.createPortForwardLocal();
	}
}
