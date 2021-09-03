package com.erfurt.magicaljewelry.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JewelItemCapability
{
    public static void register()
    {
        CapabilityManager.INSTANCE.register(ICurio.class, new Capability.IStorage<ICurio>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ICurio> capability, ICurio instance, Direction side)
            {
                return new CompoundNBT();
            }

            @Override
            public void readNBT(Capability<ICurio> capability, ICurio instance, Direction side, INBT nbt) { }
        }, JewelItemWrapper::new);
    }

    public static ICapabilityProvider createProvider(final ICurio curio)
    {
        return new Provider(curio);
    }

    public static class JewelItemWrapper implements ICurio { }

    public static class Provider implements ICapabilityProvider
    {
        final LazyOptional<ICurio> capability;

        Provider(ICurio curio)
        {
            this.capability = LazyOptional.of(() -> curio);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CuriosCapability.ITEM.orEmpty(cap, this.capability);
        }
    }
}