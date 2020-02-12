package rokolabs.com.peoplefirst.di.component;

import dagger.Component;
import rokolabs.com.peoplefirst.di.scopes.PerService;
import rokolabs.com.peoplefirst.services.FileUploadService;

@PerService
@Component(dependencies = AppComponent.class)
public interface ServiceComponent {

	void inject(FileUploadService service);
}
