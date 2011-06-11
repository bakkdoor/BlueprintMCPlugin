package de.flasht.blueprint.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.flasht.blueprint.BlueprintManager;
import de.flasht.blueprint.BlueprintPlugin;

public class BlueprintCommand implements CommandExecutor {
	protected BlueprintPlugin plugin;
	
	public BlueprintCommand(final BlueprintPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args[0].equals("start")) {
			return startBlueprint(sender);
		}
		if(args[0].equals("end")) {
			return endBlueprint(sender);
		}
		
		// unknown argument
		return false;
	}
	
	private boolean startBlueprint(CommandSender sender)
	{
		BlueprintManager.startBlueprintMode((Player) sender);
		sender.sendMessage("Blueprint mode enabled.");
		return true;		
	}

	private boolean endBlueprint(CommandSender sender)
	{
		BlueprintManager.endBlueprintMode((Player)sender);
		sender.sendMessage("Blueprint mode disabled.");
		return true;
	}
}
