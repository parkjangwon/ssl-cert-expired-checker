package org.parkjw.checker.domains.checker.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Group {

	private List<Integer> condition;

	private List<String> recipients;

	private List<String> domains;
}
