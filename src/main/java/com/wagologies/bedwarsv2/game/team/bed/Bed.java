package com.wagologies.bedwarsv2.game.team.bed;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Bed {

    private final Team team;
    private Location bedBlock1, bedBlock2;
    public boolean isBedBroken;

    public Bed(Team team)
    {
        this.team = team;
        bedBlock1 = team.getBedBlock(1);
        bedBlock2 = team.getBedBlock(2);
        isBedBroken = false;
        new BedListener(this);
    }

    public void BreakBed()
    {
        if(!isBedBroken) {
            bedBlock1.getBlock().setType(Material.AIR, false);
            bedBlock2.getBlock().setType(Material.AIR, false);
            isBedBroken = true;
            team.BreakBed();
        }
    }

    public void RespawnBed()
    {
        if(isBedBroken)
        {
            BlockState bedHead = bedBlock1.getBlock().getState();
            BlockState bedFoot = bedBlock2.getBlock().getState();
            bedFoot.setType(Material.BED_BLOCK);
            bedHead.setType(Material.BED_BLOCK);
            org.bukkit.material.Bed bedHeadData = (org.bukkit.material.Bed) bedHead.getData();
            org.bukkit.material.Bed bedFootData = (org.bukkit.material.Bed) bedFoot.getData();
            bedHeadData.setHeadOfBed(true);
            bedFootData.setHeadOfBed(false);
            bedHeadData.setFacingDirection(bedBlock2.getBlock().getFace(bedBlock1.getBlock()));
            bedFootData.setFacingDirection(bedBlock2.getBlock().getFace(bedBlock1.getBlock()));
            bedHead.setData(bedHeadData);
            bedFoot.setData(bedFootData);
            bedFoot.update(true, false);
            bedHead.update(true, true);
            isBedBroken = false;
            team.RespawnBed();
        }
    }

    /***** GETTER METHODS *****/

    public boolean getIsBedBroken() { return isBedBroken; }

    public Location getBedBlock(int number) {
        switch (number) {
            case 1:
                return bedBlock1;
            case 2:
                return bedBlock2;
            default:
                return null;
        }
    }

    public boolean IsPlayerOnTeam(Player player)
    {
        if(team.getGame().getBedwarsPlayer(player).getTeam().equals(team))
        {
            return true;
        }
        return false;
    }

    /*** END GETTER METHODS ***/
}
