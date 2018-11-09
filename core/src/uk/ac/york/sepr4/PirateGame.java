package uk.ac.york.sepr4;

import com.badlogic.gdx.Game;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.screen.MenuScreen;
import uk.ac.york.sepr4.screen.ScreenType;

public class PirateGame extends Game {

	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	
	@Override
	public void create () {
	    //switchScreen(ScreenType.MENU);

		//FOR DEVELOPMENT
		switchScreen(ScreenType.GAME);
	}

	public void switchScreen(ScreenType screenType){
		switch (screenType) {
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
            case GAME:
                if(gameScreen == null) gameScreen = new GameScreen(this);
                this.setScreen(gameScreen);
		}
	}
}
