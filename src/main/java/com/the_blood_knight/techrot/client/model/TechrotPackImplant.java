// Made with Blockbench 5.0.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports

package com.the_blood_knight.techrot.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TechrotPackImplant extends ModelBase {
	private final ModelRenderer truemain;
	public final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	public final ModelRenderer torsopack;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	public final ModelRenderer rightwing;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	public final ModelRenderer leftwing;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	public final ModelRenderer torso;
	public final ModelRenderer right_arm;
	private final ModelRenderer humanbody;
	private final ModelRenderer head2;
	private final ModelRenderer body;
	private final ModelRenderer left_armhuman;
	private final ModelRenderer right_armhuman;
	private final ModelRenderer left_leg;
	private final ModelRenderer right_leg;

	public TechrotPackImplant() {
		textureWidth = 128;
		textureHeight = 128;

		truemain = new ModelRenderer(this);
		truemain.setRotationPoint(-8.0F, 16.0F, 8.0F);


		head = new ModelRenderer(this);
		head.setRotationPoint(8.0F, -16.0F, -8.0F);
		truemain.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 0, -4.5F, -9.25F, -4.925F, 9, 5, 9, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 83, 0, -4.0F, -8.8F, -4.725F, 8, 4, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 65, 2, -0.5F, -13.25F, 0.075F, 3, 4, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 77, 4, -3.5F, -12.25F, 0.075F, 2, 3, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 39, 1, -4.5F, -4.25F, -3.975F, 9, 1, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 112, -4, -4.25F, -3.5F, -4.15F, 0, 4, 8, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 112, 0, 4.25F, -3.5F, -4.15F, 0, 4, 8, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 65, 0, -4.5F, -4.25F, -4.85F, 9, 1, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 96, 8, -11.5F, -5.475F, -4.525F, 8, 8, 0, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 106, 16, 0.5F, -8.225F, -5.1F, 3, 3, 1, -0.25F, false));
		head.cubeList.add(new ModelBox(head, 96, 8, 3.5F, -5.475F, -4.525F, 8, 8, 0, 0.0F, true));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, -1.475F, -4.525F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.6981F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 103, 0, -4.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 27, 0, -4.0F, -1.0F, -1.0F, 8, 2, 2, 0.1F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0585F, -3.3888F);
		head.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.6981F, 0.0F, 3.1416F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 27, 5, -3.5F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, -3.0915F, -4.1138F);
		head.addChild(cube_r3);
		setRotationAngle(cube_r3, -0.1745F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 27, 5, -3.5F, -1.0F, -1.0F, 7, 2, 2, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, -1.25F, 4.025F);
		head.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, -1.5708F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 112, 4, 0.0F, -2.0F, -4.0F, 0, 4, 8, 0.0F, true));

		torsopack = new ModelRenderer(this);
		torsopack.setRotationPoint(1.201F, -10.559F, -3.925F);
		truemain.addChild(torsopack);
		

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(6.799F, 2.8135F, -0.1748F);
		torsopack.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.8262F, 0.5956F, 0.5468F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 15, 27, -2.0F, 1.25F, -1.0F, 3, 0, 3, 0.0F, false));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(6.799F, -1.3457F, -2.475F);
		torsopack.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.8262F, 0.5956F, 0.5468F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 24, 31, -2.0F, 2.2F, -2.0F, 4, 4, 4, 0.0F, false));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(6.799F, -1.3457F, -2.475F);
		torsopack.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.5299F, 0.7119F, 0.3655F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 27, -3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false));

		rightwing = new ModelRenderer(this);
		rightwing.setRotationPoint(6.799F, -1.3457F, -2.475F);
		torsopack.addChild(rightwing);
		

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(-9.1924F, -4.5209F, 9.6079F);
		rightwing.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.5299F, -0.7119F, -0.3655F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 44, 10, 0.0F, -1.5F, -8.0F, 0, 11, 20, 0.0F, true));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(-7.0534F, -2.3894F, 6.6831F);
		rightwing.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.7854F, -0.3927F, 1.5708F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 72, 27, -0.5F, 0.0F, -7.0F, 1, 0, 14, 0.0F, true));
		cube_r9.cubeList.add(new ModelBox(cube_r9, 72, 27, -0.5F, -1.95F, -7.0F, 1, 0, 14, 0.0F, true));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.0F, 0.0F, 0.0F);
		rightwing.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.5299F, -0.7119F, -0.3655F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 70, 27, 0.0F, 0.85F, 3.0F, 2, 0, 14, 0.0F, true));
		cube_r10.cubeList.add(new ModelBox(cube_r10, 26, 27, 0.0F, -2.0F, 3.0F, 2, 3, 14, 0.0F, true));

		leftwing = new ModelRenderer(this);
		leftwing.setRotationPoint(6.799F, -1.3457F, -2.475F);
		torsopack.addChild(leftwing);
		

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(9.1924F, -4.5209F, 9.6079F);
		leftwing.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.5299F, 0.7119F, 0.3655F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 44, 10, 0.0F, -1.5F, -8.0F, 0, 11, 20, 0.0F, false));

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(7.0534F, -2.3894F, 6.6831F);
		leftwing.addChild(cube_r12);
		setRotationAngle(cube_r12, -0.7854F, 0.3927F, -1.5708F);
		cube_r12.cubeList.add(new ModelBox(cube_r12, 72, 27, -0.5F, 0.0F, -7.0F, 1, 0, 14, 0.0F, false));
		cube_r12.cubeList.add(new ModelBox(cube_r12, 72, 27, -0.5F, -1.95F, -7.0F, 1, 0, 14, 0.0F, false));

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(0.0F, 0.0F, 0.0F);
		leftwing.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.5299F, 0.7119F, 0.3655F);
		cube_r13.cubeList.add(new ModelBox(cube_r13, 70, 27, -2.0F, 0.85F, 3.0F, 2, 0, 14, 0.0F, false));
		cube_r13.cubeList.add(new ModelBox(cube_r13, 120, 16, -2.0F, 0.825F, 4.0F, 2, 6, 2, 0.0F, false));
		cube_r13.cubeList.add(new ModelBox(cube_r13, 120, 16, -2.0F, 0.825F, 9.0F, 2, 6, 2, 0.0F, false));
		cube_r13.cubeList.add(new ModelBox(cube_r13, 120, 16, -2.0F, 0.825F, 14.0F, 2, 6, 2, 0.0F, false));
		cube_r13.cubeList.add(new ModelBox(cube_r13, 26, 27, -2.0F, -2.0F, 3.0F, 2, 3, 14, 0.0F, false));

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.0F, 0.0F, 0.0F);
		leftwing.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.5299F, -0.7119F, -0.3655F);
		cube_r14.cubeList.add(new ModelBox(cube_r14, 120, 16, 0.0F, 0.825F, 14.0F, 2, 6, 2, 0.0F, true));
		cube_r14.cubeList.add(new ModelBox(cube_r14, 120, 16, 0.0F, 0.825F, 9.0F, 2, 6, 2, 0.0F, true));
		cube_r14.cubeList.add(new ModelBox(cube_r14, 120, 16, 0.0F, 0.825F, 4.0F, 2, 6, 2, 0.0F, true));

		torso = new ModelRenderer(this);
		torso.setRotationPoint(8.0F, -16.0F, -8.0F);
		truemain.addChild(torso);
		torso.cubeList.add(new ModelBox(torso, 0, 14, -5.0F, -0.2362F, -2.5F, 10, 8, 5, -0.25F, false));
		torso.cubeList.add(new ModelBox(torso, 54, 18, -4.0F, 5.9888F, -2.0F, 8, 5, 4, 0.1F, false));
		torso.cubeList.add(new ModelBox(torso, 30, 18, -4.5F, 0.7638F, -2.025F, 9, 6, 3, 0.0F, false));

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(3.0F, -14.0F, -8.0F);
		truemain.addChild(right_arm);
		right_arm.cubeList.add(new ModelBox(right_arm, 78, 11, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));
		right_arm.cubeList.add(new ModelBox(right_arm, 94, 16, -2.875F, -1.925F, -1.3F, 3, 10, 3, 0.0F, false));

		humanbody = new ModelRenderer(this);
		humanbody.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		head2 = new ModelRenderer(this);
		head2.setRotationPoint(0.0F, 0.0F, 0.0F);
		humanbody.addChild(head2);
		head2.cubeList.add(new ModelBox(head2, 96, 112, -4.0F, -32.0F, -4.0F, 8, 8, 8, 0.0F, false));

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -24.0F, 0.0F);
		humanbody.addChild(body);
		body.cubeList.add(new ModelBox(body, 104, 112, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

		left_armhuman = new ModelRenderer(this);
		left_armhuman.setRotationPoint(5.0F, -22.0F, 0.0F);
		humanbody.addChild(left_armhuman);
		left_armhuman.cubeList.add(new ModelBox(left_armhuman, 107, 112, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));

		right_armhuman = new ModelRenderer(this);
		right_armhuman.setRotationPoint(-5.0F, -22.0F, 0.0F);
		humanbody.addChild(right_armhuman);
		right_armhuman.cubeList.add(new ModelBox(right_armhuman, 104, 112, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(2.0F, -12.0F, 0.0F);
		humanbody.addChild(left_leg);
		left_leg.cubeList.add(new ModelBox(left_leg, 112, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(-2.0F, -12.0F, 0.0F);
		humanbody.addChild(right_leg);
		right_leg.cubeList.add(new ModelBox(right_leg, 104, 112, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		truemain.render(f5);
		humanbody.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}


}