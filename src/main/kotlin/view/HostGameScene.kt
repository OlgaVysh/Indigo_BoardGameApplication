package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label

class HostGameScene (private val rootService: RootService) : MenuScene(990, 1080) {
    private val game = rootService.currentGame

    private val hostLabel = Label (42, 105, 900,116,"Host Game",120)
    private val nameLabel = Label(80, 321,300,58,"Name :",48)
    private val gameIdLabel = Label (80, 486,350,116,"Game ID :",48)

    private val hostName: TextField = TextField(
        width = 454,
        height = 69,
        posX = 390,
        posY = 320,
    ).apply {
        onKeyTyped = {
            hostGameButton.isDisabled = this.text.isBlank()
        }

    }
    private val gameId: TextField = TextField(
        width = 454,
        height = 69,
        posX = 390,
        posY = 510
    )
    private val hostGameButton = Button(247, 698,532,207,"Host game",48)

    init {
        opacity = 0.5
        addComponents(
            hostLabel,
            nameLabel,
            gameIdLabel,
            hostName,
            gameId,
            hostGameButton
        )
    }


}