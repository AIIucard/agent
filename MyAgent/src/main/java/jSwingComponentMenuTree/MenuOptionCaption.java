/*
 * Copyright (c) 2016 SSI Schaefer Noell GmbH
 */

package main.java.jSwingComponentMenuTree;

public enum MenuOptionCaption {
	// Pallet
	GOODS_RECEIPT_PAL_INFEED_PIG01SP11("Wareneingang PIG01SP11"), //
	GOODS_RECEIPT_PAL_CONTOUR_CHECK_PIG01IP12("Wareneingang Konturenkontrolle PIG01IP12"), //
	GOODS_RECEIPT_PAL_SHUTTLE_PIG01SV01("Wareneingang Verschiebewagen PIG01SV01"), //
	PAL_STORAGE("Paletten Lager"), //
	GOODS_ISSUE_PAL_EMPTY_PALLETS_BUFFER("Warenausgang Leerpaletten Buffer"), //
	GOODS_ISSUE_PAL_CONTOUR_CHECK_STATION_1_TO_4("Warenausgang Konturenkontrolle"), //
	GOODS_ISSUE_PAL_WEIGHT_CHECK_POG01DP11("Warenausgang Gewichtskontrolle POG01DP11"), //

	// Bin
	GOODS_RECEIPT_BIN_INFEED_BIG1000X("Wareneingang BIG1000BinInfeedPointConveyor"), //
	GOODS_RECEIPT_BIN_CONTOUR_CHECK_BIG1000SC0111("Wareneingang Konturenkontrolle BIG1000SC0111"), //
	GOODS_RECEIPT_BIN_NOK_CS51FP018PL077("Wareneingang NIO CS51FP018PL077"), //
	BIN_STORAGE("Behälter Lager"), //
	GOODS_ISSUE_BIN_CONTOUR_CHECK_STATION_1_TO_4("Warenausgang Konturenkontrolle"), //
	GOODS_ISSUE_BIN_NOK_STATION_2_BPG0100JM0122("Warenausgang Station 2 NIO BPG0100JM0122"), //
	GOODS_ISSUE_BIN_NOK_STATION_4_BPG0200JM0122("Warenausgang Station 4 NIO BPG0200JM0122");

	protected final String label;

	private MenuOptionCaption(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
