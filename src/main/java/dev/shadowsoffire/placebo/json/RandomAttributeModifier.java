package dev.shadowsoffire.placebo.json;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.codec.PlaceboCodecs;
import dev.shadowsoffire.placebo.util.StepFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

import java.lang.reflect.Type;
import java.util.UUID;

public record RandomAttributeModifier(Attribute attribute, Operation op, StepFunction value, UUID id) {

    public static Codec<RandomAttributeModifier> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(a -> a.attribute),
                    PlaceboCodecs.enumCodec(Operation.class).fieldOf("operation").forGetter(a -> a.op),
                    StepFunction.CODEC.fieldOf("value").forGetter(a -> a.value))
            .apply(inst, RandomAttributeModifier::new));

    public static Codec<RandomAttributeModifier> CONSTANT_CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(a -> a.attribute),
                    PlaceboCodecs.enumCodec(Operation.class).fieldOf("operation").forGetter(a -> a.op),
                    StepFunction.CONSTANT_CODEC.fieldOf("value").forGetter(a -> a.value))
            .apply(inst, RandomAttributeModifier::new));

    /**
     * Creates a Random Attribute Modifier. A UUID will be randomly generated.
     *
     * @param attribute The attribute the generated modifier will be applicable to.
     * @param op        The operation of the generated modifier.
     * @param value     The value range for the generated modifier.
     */
    public RandomAttributeModifier(Attribute attribute, Operation op, StepFunction value) {
        this(attribute, op, value, UUID.randomUUID());
    }

    public void apply(RandomSource rand, LivingEntity entity) {
        if (entity == null) throw new RuntimeException("Attempted to apply a random attribute modifier to a null entity!");
        AttributeModifier modif = this.create(rand);
        AttributeInstance inst = entity.getAttribute(this.attribute);
        if (inst == null) {
            Placebo.LOGGER.trace(String.format("Attempted to apply a random attribute modifier to an entity (%s) that does not have that attribute (%s)!",
                    EntityType.getKey(entity.getType()), BuiltInRegistries.ATTRIBUTE.getKey(this.attribute)));
            return;
        }
        // Check if the modifier already exists before adding it
        if (inst.getModifier(modif.getId()) == null) {
            inst.addPermanentModifier(modif);
        } else {
            Placebo.LOGGER.warn(String.format("Duplicate attribute modifier detected! Entity: %s, Attribute: %s, Modifier: %s",
                    EntityType.getKey(entity.getType()), BuiltInRegistries.ATTRIBUTE.getKey(this.attribute), modif.getId()));
        }
    }

    public AttributeModifier create(RandomSource rand) {
        return new AttributeModifier(this.id, "placebo_random_modifier_" + this.attribute.getDescriptionId(), this.value.get(rand.nextFloat()), this.op);
    }

    public AttributeModifier create(String name, RandomSource rand) {
        return new AttributeModifier(name, this.value.get(rand.nextFloat()), this.op);
    }

    public AttributeModifier createDeterministic() {
        return new AttributeModifier(this.id, "placebo_random_modifier_" + this.attribute.getDescriptionId(), this.value.min(), this.op);
    }

    public AttributeModifier createDeterministic(String name) {
        return new AttributeModifier(this.id, name, this.value.min(), this.op);
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public Operation getOp() {
        return this.op;
    }

    public StepFunction getValue() {
        return this.value;
    }

    public static class Deserializer implements JsonDeserializer<RandomAttributeModifier>, JsonSerializer<RandomAttributeModifier> {

        @Override
        public RandomAttributeModifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String _attribute = obj.get("attribute").getAsString();
            Operation op = ctx.deserialize(obj.get("operation"), Operation.class);
            StepFunction value;
            if (obj.get("value").isJsonObject()) {
                JsonObject valueObj = GsonHelper.getAsJsonObject(obj, "value");
                value = ctx.deserialize(valueObj, StepFunction.class);
            }
            else {
                float v = GsonHelper.getAsFloat(obj, "value");
                value = new StepFunction(v, 1, 0);
            }
            Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation(_attribute));
            if (attribute == null || value == null || op == null) throw new JsonParseException("Attempted to deserialize invalid RandomAttributeModifier: " + json.toString());
            return new RandomAttributeModifier(attribute, op, value);
        }

        @Override
        public JsonElement serialize(RandomAttributeModifier src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("attribute", BuiltInRegistries.ATTRIBUTE.getKey(src.attribute).toString());
            obj.addProperty("operation", src.op.name());
            StepFunction range = src.value;
            if (range.min() == range.max()) {
                obj.addProperty("value", range.min());
            }
            else {
                obj.add("value", context.serialize(src.value));
            }
            return obj;
        }
    }
}
