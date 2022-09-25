package net.runelite.api;

public interface SceneEntity extends Locatable, Identifiable, Interactable, EntityNameable
{
    long getTag();
}
