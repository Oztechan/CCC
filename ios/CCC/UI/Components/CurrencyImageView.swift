//
//  CurrencyImageView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.05.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct CurrencyImageView: View {
    let imageName: String

    var body: some View {
        Image(uiImage: imageName.getImage())
            .resize(widthAndHeight: 36.cp())
            .shadow(radius: 3)
    }
}
