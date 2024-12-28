package com.jkpr.chinesecheckers.server.UI;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jkpr.chinesecheckers.server.ChoiceBase;

public class ServerWindow extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    private TextButton submit;

    private SelectBox<String> typeSelect;
    private SelectBox<String> numberSelect;

    private GameOptions options;

    public ServerWindow(GameOptions options) {
        this.options = options;
    }


    @Override
    public void create() {
        // Tworzenie stage i załadowanie skinu
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Window window = new Window("", skin);

        //window options
        window.setSize(500, 500);
        window.setPosition(Gdx.graphics.getWidth() / 2f - window.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - window.getHeight() / 2f);

        // Dodanie okna do sceny
        ChoiceBase base=new ChoiceBase();
        String[] typeOptions = base.getKeys();
        final String[][] playerOptions = {{"wybierz liczbe"}};

        typeSelect = new SelectBox<>(skin);
        numberSelect = new SelectBox<>(skin);
        submit = new TextButton("submit",skin);

        typeSelect.setHeight(30);
        typeSelect.setWidth(200);

        numberSelect.setHeight(30);
        numberSelect.setWidth(200);

        typeSelect.setItems(typeOptions);
        numberSelect.setItems(playerOptions[0][0]);

        typeSelect.setPosition(10, 400);
        numberSelect.setPosition(10, 300);
        submit.setPosition(50,100);


        window.addActor(typeSelect);

        window.addActor(numberSelect);

        window.addActor(submit);

        stage.addActor(window);

        typeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = typeSelect.getSelected();

                playerOptions[0]=base.getArray(selected);
                numberSelect.setItems(playerOptions[0]);
            }
        });
        numberSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = numberSelect.getSelected();
            }
        });
        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(typeSelect.getSelected()+" "+numberSelect.getSelected());

                if(!typeSelect.getSelected().equals("Wybierz typ")
                        &&!numberSelect.getSelected().equals("wybierz liczbe")) {
                    options.setData(typeSelect.getSelected(), numberSelect.getSelected());
                    Gdx.app.exit();
                }
            }
        });
    }

    @Override
    public void render() {
        // Czyszczenie ekranu
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Aktualizacja i renderowanie sceny
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
