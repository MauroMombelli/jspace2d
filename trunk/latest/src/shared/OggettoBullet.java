package shared;

public class OggettoBullet extends Oggetto2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5735099940575927022L;

	public OggettoBullet(int id) {
		super(id);
		modelName="Bullet1";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 2f );
		density=20;
	}

	public OggettoBullet(OggettoBullet o) {
		super(o);
		modelName="Bullet1";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 2f );
		density=20;
	}

}
