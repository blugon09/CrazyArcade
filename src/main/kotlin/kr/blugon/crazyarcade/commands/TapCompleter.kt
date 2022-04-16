package kr.blugon.crazyarcade.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.Collections

class TapCompleter : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if(command.name != "crazyarcade") return null
        if(args.size == 1) {
            val returns = arrayListOf("start", "stop", "waterball", "reset")

            val last = arrayListOf<String>()
            for (r in returns) {
                if(r.startsWith(args[0].lowercase())) last.add(r)
            }
            return last
        }
        return Collections.emptyList()
    }
}