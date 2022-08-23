package com.cognizant.pms.login.apiResponse;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8082183710656797968L;
	private int status;
    private String message;
    private Object data;
}
