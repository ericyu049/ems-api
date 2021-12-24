package com.aiamadeus.ems.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Response  {
	private int rspCde;
	private Object rspMsg;
}
