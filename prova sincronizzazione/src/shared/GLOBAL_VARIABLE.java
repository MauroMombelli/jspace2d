package shared;

public class GLOBAL_VARIABLE {
	private static float conversion = 20;
	
	public static float convertToPhysicEngineUnit(float number){
		number=number/conversion ;
		return number;	
	}
	
	public static float convertFromPhysicEngineUnit(float number){
		number=number*conversion ;
		return number;	
	}

}
