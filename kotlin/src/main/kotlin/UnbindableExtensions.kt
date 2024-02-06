import me.ashydev.binding.IUnbindable

/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

operator fun IUnbindable.minusAssign(target: IUnbindable) {
    this.unbindFrom(target)
}