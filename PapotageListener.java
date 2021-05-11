import java.util.EventListener;

public interface PapotageListener extends EventListener{
	
	void eventReceptionMessage(PapotageEvent pe);
}
