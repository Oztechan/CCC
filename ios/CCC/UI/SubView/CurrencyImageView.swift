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
            .resizable()
            .frame(width: 36.cp(), height: 36.cp(), alignment: .center)
            .shadow(radius: 3)
    }
}
