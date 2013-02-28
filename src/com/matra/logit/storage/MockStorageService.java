package com.matra.logit.storage;

public class MockStorageService {

	private Exercise[] mockStorage;
	
	//TODO : Remove this later
	public static final String loremIpsum = "Lorem ipsum ad his scripta blandit partiendo," +
			" eum fastidii accumsan euripidis in, eum liber hendrerit an. " +
			"Qui ut wisi vocibus suscipiantur, quo dicit ridens inciderint id. " +
			"Quo mundi lobortis reformidans eu, legimus senserit definiebas an eos. " +
			"Eu sit tincidunt incorrupte definitionem, vis mutat affert percipit cu, eirmod consectetuer signiferumque eu per. " +
			"In usu latine equidem dolores. Quo no falli viris intellegam, ut fugit veritus placerat per. " +
			"Ius id vidit volumus mandamus, vide veritus democritum te nec, ei eos debet libris consulatu. " +
			"No mei ferri graeco dicunt, ad cum veri accommodare. Sed at malis omnesque delicata, usu et iusto zzril meliore. " +
			"Dicunt maiorum eloquentiam cum cu, sit summo dolor essent te. Ne quodsi nusquam legendos has, ea dicit voluptua eloquentiam pro," +
			" ad sit quas qualisque. Eos vocibus deserunt quaestio ei. Blandit incorrupte quaerendum in quo, nibh impedit id vis," +
			" vel no nullam semper audiam.";
	//---------------------------
	
	private static MockStorageService instance = null;
	public static MockStorageService getInstance()
	{
		if(instance == null)
		{
			instance = new MockStorageService();
		}
		return instance;
	}
	
	private MockStorageService()
	{
		mockStorage = new Exercise[10];
		mockStorage[0] = new Exercise("Pushup",loremIpsum);
		mockStorage[1] = new Exercise("Laterals", loremIpsum);
		mockStorage[2] = new Exercise("Deadlift", loremIpsum);
		mockStorage[3] = new Exercise("Rows", loremIpsum);
		mockStorage[4] = new Exercise("Swimming Laps", loremIpsum);
		mockStorage[5] = new Exercise("Barbell Lift", loremIpsum);
		mockStorage[6] = new Exercise("Bulgarian Bag", loremIpsum);
		mockStorage[7] = new Exercise("Bent Row", loremIpsum);
		mockStorage[8] = new Exercise("Cycling", loremIpsum);
		mockStorage[9] = new Exercise("Hiking", loremIpsum);
	}
	
	//Idea : This will return later an exercise list, whole. Up to the control to filter it. 
	public Exercise[] getExerciseList()
	{
		return mockStorage;
	}
	
}