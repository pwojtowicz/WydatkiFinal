package pl.wppiotrek.wydatki.basepackage.enums;

public enum ParameterDataType {
	Text, Number, List, ChoiseBoolean;

	public static ParameterDataType fromInteger(int x) {
		switch (x) {
		case 1:
			return Text;
		case 2:
			return Number;
		case 3:
			return List;
		case 4:
			return ChoiseBoolean;
		}
		return null;
	}

	public static int fromType(ParameterDataType itemType) {
		switch (itemType) {
		case Text:
			return 1;
		case Number:
			return 2;
		case List:
			return 3;
		case ChoiseBoolean:
			return 4;
		default:
			return 0;
		}
	}
}
