package com.cognizant.pms.login.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1978864029806140474L;
	private Date timestamp;
    private int status;
    private String error;
    private List<String> details;
    private String path;
}