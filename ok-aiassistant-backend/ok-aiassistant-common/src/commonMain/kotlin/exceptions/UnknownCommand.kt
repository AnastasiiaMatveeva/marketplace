import models.AICommand


class UnknownCommand(AICommand: AICommand) : Throwable("Wrong command $AICommand at mapping toTransport stage")
